// Copyright 2016 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.ads.adwords.keywordoptimizer;

import com.google.api.ads.adwords.axis.v201509.cm.Money;
import com.google.api.ads.adwords.axis.v201509.cm.Paging;
import com.google.api.ads.adwords.axis.v201509.o.AttributeType;
import com.google.api.ads.adwords.axis.v201509.o.IdeaType;
import com.google.api.ads.adwords.axis.v201509.o.RelatedToQuerySearchParameter;
import com.google.api.ads.adwords.axis.v201509.o.RequestType;
import com.google.api.ads.adwords.axis.v201509.o.SearchParameter;
import com.google.api.ads.adwords.axis.v201509.o.TargetingIdeaSelector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Creates a set of seed keywords derived from a list of example search terms.
 */
public class TisSearchTermsSeedGenerator extends TisBasedSeedGenerator {
  private final Set<String> seedKeywords;

  /**
   * Creates a new {@link TisSearchTermsSeedGenerator}. Please note that example keywords have to be
   * added separately.
   * 
   * @param context holding shared objects during the optimization process
   * @param maxCpc maximum cpc to be used for keyword evaluation
   */
  public TisSearchTermsSeedGenerator(OptimizationContext context, @Nullable Money maxCpc) {
    super(context, maxCpc);
    seedKeywords = new HashSet<String>();
  }

  @Override
  protected TargetingIdeaSelector getSelector() {
    TargetingIdeaSelector selector = new TargetingIdeaSelector();
    selector.setRequestType(RequestType.IDEAS);
    selector.setIdeaType(IdeaType.KEYWORD);
    selector.setPaging(new Paging(0, PAGE_SIZE));

    selector.setRequestedAttributeTypes(new AttributeType[] {AttributeType.KEYWORD_TEXT});

    List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();

    // Get ideas related to query search parameter.
    RelatedToQuerySearchParameter relatedToQuerySearchParameter =
        new RelatedToQuerySearchParameter();
    relatedToQuerySearchParameter.setQueries(seedKeywords.toArray(new String[] {}));
    searchParameters.add(relatedToQuerySearchParameter);

    // Now add all other criteria.
    searchParameters.addAll(KeywordOptimizerUtil.toSearchParameters(getAdditionalCriteria()));

    selector.setSearchParameters(searchParameters.toArray(new SearchParameter[] {}));

    return selector;
  }

  /**
   * Adds a search term to the generator.
   * 
   * @param keyword the search term to the generator to be added
   */
  public void addSearchTerm(String keyword) {
    seedKeywords.add(keyword);
  }
}
